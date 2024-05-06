package ma.enset.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.DTOs.CustomerDTO;
import ma.enset.ebankingbackend.exeptions.CustomerNotFoundException;
import ma.enset.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> Customers(){

        return bankAccountService.listCustomer();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer( @PathVariable(name="id") Long ConsumerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(ConsumerId);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException{
         return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{id}")
        public CustomerDTO UpdateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customerDto){
        return bankAccountService.updateCustomer(customerDto);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id ){
         bankAccountService.deleteCustomer(id);
    }


}
