package com.eventvista.event_vista.service;

import com.eventvista.event_vista.data.VendorRepository;
import com.eventvista.event_vista.exception.VendorNotFoundException;
import com.eventvista.event_vista.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    // Constructor
    @Autowired
    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }


    // Query methods
    public Vendor addVendor(Vendor vendor) {
        vendor.setName(UUID.randomUUID().toString());
        return vendorRepository.save(vendor);
    }

    public Vendor findVendorById(Integer id) {
        return vendorRepository.findVendorById(id).orElseThrow(() -> new VendorNotFoundException("Vendor by id " + id + " was not found."));
    }

    public List<Vendor> findAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor updateVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public void deleteVendor(Integer id) {
        vendorRepository.deleteVendorById(id);
    }

}
