package org.alilopez.service;


import org.alilopez.model.Address;
import org.alilopez.repository.AddressRepository;

import java.sql.SQLException;
import java.util.List;

public class AddressService {
    private AddressRepository addressRepository;
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    public List<Address> getMyAddresses(int id_usuario) throws SQLException {
        return addressRepository.getMyAddresses(id_usuario);
    }

    public Address createAddress(Address address) throws SQLException {
        return addressRepository.save(address);
    }

    public Address getAddressById(int id_direccion) throws SQLException {
        Address address = addressRepository.findById(id_direccion);
        return address;
        }

    public void updateAddress(int id_Address, Address address) throws SQLException {
        addressRepository.updateAddress(id_Address, address);
    }
    public void deleteAddress(int id_address) throws SQLException {
        addressRepository.deleteAddress(id_address);
    }
    }

