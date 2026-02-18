package com.project.figureout.service;

import com.project.figureout.dto.AddressDTO;
import com.project.figureout.dto.CountryDTO;
import com.project.figureout.dto.StateDTO;
import com.project.figureout.model.Address;
import com.project.figureout.model.Client;
import com.project.figureout.model.Country;
import com.project.figureout.model.State;
import com.project.figureout.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AddressService {

    @Autowired
    private StateAndCountryService stateAndCountryService;

    @Autowired
    private AddressRepository addressRepository;

    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

    public Address getAddressById(long id) {
        return addressRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Endereço não encontrado com base no ID."));
    }

    public void deleteAddress(long id) {
        addressRepository.deleteById(id);
    }

    public void insertDataIntoAddress(Address address, AddressDTO addressDTO) {
        address.setDeliveryAddress(addressDTO.isDeliveryAddress());
        address.setChargingAddress(addressDTO.isChargingAddress());
        address.setNickname(addressDTO.getNickname());
        address.setTypeOfResidence(addressDTO.getTypeOfResidence());
        address.setAddressing(addressDTO.getAddressing());
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setNeighbourhood(addressDTO.getNeighbourhood());
        address.setAddressingType(addressDTO.getAddressingType());
        address.setCep(treatMaskedCep(addressDTO.getCep()));
        address.setCity(addressDTO.getCity());
        address.setObservation(addressDTO.getObservation());
        // State and Country SHOULD be injected via the method calling this method
    }

    public void registerAddress(Client client, AddressDTO addressDTO) {
        Address address = new Address();

        insertDataIntoAddress(address, addressDTO);

        State state = new State();
        Country country = new Country();
        state.setId(addressDTO.getStateDTO().getId());
        country.setId(addressDTO.getCountryDTO().getId());
        address.setState(state);
        address.setCountry(country);

        client.getAddresses().add(address);
        address.setClient(client);

        saveAddress(address);
    }

    public void updateAddress(long id, AddressDTO addressDTO) {
        Address address = getAddressById(id);

        insertDataIntoAddress(address, addressDTO);

        State state = stateAndCountryService.getStateById(addressDTO.getStateDTO().getId());
        Country country = stateAndCountryService.getCountryById(addressDTO.getCountryDTO().getId());
        address.setState(state);
        address.setCountry(country);

        saveAddress(address);
    }

    public void populateAddressDTO(AddressDTO addressDTO, Address address) {
        addressDTO.setDeliveryAddress(address.isDeliveryAddress());
        addressDTO.setChargingAddress(address.isChargingAddress());
        addressDTO.setNickname(address.getNickname());
        addressDTO.setTypeOfResidence(address.getTypeOfResidence());
        addressDTO.setAddressing(address.getAddressing());
        addressDTO.setHouseNumber(address.getHouseNumber());
        addressDTO.setNeighbourhood(address.getNeighbourhood());
        addressDTO.setAddressingType(address.getAddressingType());
        addressDTO.setCep(addMaskToCep(address.getCep()));
        addressDTO.setCity(address.getCity());
        addressDTO.setObservation(address.getObservation());

        StateDTO stateDTO = new StateDTO();
        stateDTO.setId(address.getState().getId());

        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(address.getCountry().getId());

        addressDTO.setStateDTO(stateDTO);
        addressDTO.setCountryDTO(countryDTO);
    }

    public String treatMaskedCep(String cep) {
        String treatedCep;

        treatedCep = cep.replace("-", "");

        return treatedCep;
    }

    public String addMaskToCep(String cep) {
        return cep.replaceAll("(\\d{5})(.*)", "$1-$2");
    }


}
