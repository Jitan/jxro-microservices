package se.jroc.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.jroc.clients.fraud.FraudClient;
import se.jroc.clients.fraud.FraudCheckResponse;
import se.jroc.clients.notification.NotificationClient;
import se.jroc.clients.notification.NotificationRequest;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

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
        } else {
            // todo: make it async, i.e add to queue
            notificationClient.sendNotification(
                    new NotificationRequest(customer.getId(), customer.getEmail(), "Welcome home"));
        }



    }
}
