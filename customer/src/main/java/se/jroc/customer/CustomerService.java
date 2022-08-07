package se.jroc.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.jroc.amqp.RabbitMQMessageProducer;
import se.jroc.clients.fraud.FraudClient;
import se.jroc.clients.fraud.FraudCheckResponse;
import se.jroc.clients.notification.NotificationClient;
import se.jroc.clients.notification.NotificationRequest;
@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) throws IllegalAccessException {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalAccessException("Fraudster");
        }

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Welcome home %s", customer.getFirstName()));

        rabbitMQMessageProducer.publish(notificationRequest, "internal.exchange", "internal.notification.routing-key");
    }
}
