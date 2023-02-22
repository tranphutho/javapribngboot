package com.app.appdemo.controllers;

import com.app.appdemo.models.Product;
import com.app.appdemo.repositories.ProductRepository;
import com.app.appdemo.repositories.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductionController {

    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    List<Product> getAllProducts()
    {
        return  repository.findAll();
    }

    @GetMapping("/{id}")
    //let's return object data: data, message, status
    ResponseEntity<ResponseObject> findbyId(@PathVariable Long id){
        Optional<Product> foundProduct=repository.findById(id);
        if (foundProduct.isPresent())
        {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Query product successfully",foundProduct));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("falsed","Can not find product with id: "+id,"foundProduct"));
        }
        //return repository.findById(id).orElseThrow(()->new RuntimeException("Cannot find product with id=" + id));
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newproduct){

        List<Product> foundProducts= repository.findByProductName(newproduct.getProductName().trim());
        if(foundProducts.size()>0)
        {
            return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body( new ResponseObject("falsed","Product name already taken",""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Insert product successfully",repository.save(newproduct)));
    }

    @PutMapping("{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct,@PathVariable Long id){
        Product updateProduct = repository.findById(id).map(
                product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    product.setUrl(newProduct.getUrl());
                    return repository.save(product);

                }).orElseGet(() -> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body( new ResponseObject("ok","Update Product successfully",updateProduct));
    }

    @DeleteMapping("{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id)
    {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent())
        {
            repository.delete(product.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","Delete Product successfully",product));

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("false","Cannot find product succesfully","")
            );
        }
    }
}