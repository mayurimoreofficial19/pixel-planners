package com.eventvista.event_vista.data;

import com.eventvista.event_vista.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    void deleteVendorById(Integer id);

    Optional<Vendor> findVendorById(Integer id);

    Optional<Vendor> findVendorByService(List<Service> services);
}
