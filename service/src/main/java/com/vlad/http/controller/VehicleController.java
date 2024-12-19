package com.vlad.http.controller;

import com.vlad.dto.PageResponse;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.dto.vehicle.VehicleCreateDto;
import com.vlad.dto.vehicle.VehicleEditDto;
import com.vlad.dto.vehicle.VehicleReadDto;
import com.vlad.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public String findAll(Model model, VehicleFilterDto filter, Pageable pageable) {
        Page<VehicleReadDto> page = vehicleService.findAll(filter, pageable);
        model.addAttribute("vehicles", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "vehicle/vehicles";
    }

    @GetMapping({"/{id}"})
    public String findById(@PathVariable("id") Long id, Model model) {
        return vehicleService.findById(id)
                .map(vehicle -> {
                    model.addAttribute("vehicle", vehicle);
                    return "vehicle/vehicle";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/create")
    public String create(Model model, VehicleCreateDto vehicleCreateDto) {
        model.addAttribute("vehicle", vehicleCreateDto);
        return "vehicle/create";
    }

    @PostMapping
    public String save(@ModelAttribute("vehicle") VehicleCreateDto vehicleCreateDto, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "vehicle/create";
        }
        vehicleService.save(vehicleCreateDto);
        return "redirect:/vehicles";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute("vehicle") VehicleEditDto vehicleEditDto) {
        return vehicleService.update(id, vehicleEditDto)
                .map(it -> "redirect:/vehicles/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!vehicleService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/vehicles";
    }
}
