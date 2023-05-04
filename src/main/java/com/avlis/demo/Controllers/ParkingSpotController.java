package com.avlis.demo.Controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avlis.demo.dtos.ParkingSpotDto;
import com.avlis.demo.entites.ParkingSpotModel;
import com.avlis.demo.services.ParkingSpotService;

import jakarta.validation.Valid;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    
    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping //metodo post publico
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
        //<object> pq podem existir diferentes tipos de objetos que podem ser enviados
        //o DTO será chamado e o metodo só irá aceitar quando os campos forem preenchidos
        //@requestbody é utilizado para receber o dado em JSON
        //@valid faz com q as validações no DTO sejam seguidas 


        //verificações para a vaga do carro antes da instancia
            //colocar isso em um validation deopis

        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate car is already in use!");
        }

        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!!");
        }

        if(parkingSpotService.existsByApartmantAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registred for this apartment/block");
        }

        //instancia
        var parkingSpotModel = new ParkingSpotModel();
        //instancia um model
        //em escopo fechado pode usar o "var", se n deveria usar o "ParkinSporModel" no lugar

        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        //converte o DTO para model

        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        //seta a data de registro

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }


    @GetMapping //listagem geral
    public ResponseEntity <Object> getAllParkingSpots(){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }

    @GetMapping("/{id}") //listagem específica
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){

        Optional<ParkingSpotModel> parkingSpotModeOptional = parkingSpotService.findById(id);
        if(!parkingSpotModeOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModeOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModeOptional = parkingSpotService.findById(id);

        if(!parkingSpotModeOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }

        parkingSpotService.delete(parkingSpotModeOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("parking Spot deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        Optional<ParkingSpotModel> parkingSpotModeOptional = parkingSpotService.findById(id);
        if(!parkingSpotModeOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel); //copia os dados que foram atualizados
        parkingSpotModel.setId(parkingSpotModeOptional.get().getId()); //pega o id pra associar
        parkingSpotModel.setRegistrationDate(parkingSpotModeOptional.get().getRegistrationDate()); //pega a mesma data registada

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));

    }
}
