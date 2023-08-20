package ru.skyeng.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import ru.skyeng.postservice.model.Address;
import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.dto.SenderAddress;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("select a from Address a " +
            "where a.index=?1 and a.city=?2 and a.street=?3 and a.houseNumber=?4 and a.flatNumber=?5")
    Optional<Address> getAddressBySenderAddress(int index, String city, String street, int houseNum, int flatNum);
}
