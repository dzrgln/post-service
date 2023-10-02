package ru.postservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.postservice.model.Address;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.postservice.util.Fixtures.getAddress;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    Address address = new Address();

    @BeforeEach
    private void addAddress() {
        Address address = getAddress();
        addressRepository.save(address);
    }

    @AfterEach
    private void deleteAll() {
        addressRepository.deleteAll();
    }

    @Test
    void getAddressBySenderAddress() {
        final Optional<Address> result = addressRepository.getAddressBySenderAddress(address.getIndex(), address.getCity(),
                address.getStreet(), address.getHouseNumber(), address.getFlatNumber());

        if (result.isPresent()) {
            assertEquals(address.getCity(), result.get().getCity());
            assertEquals(address.getFlatNumber(), result.get().getFlatNumber());
        }
    }
}
