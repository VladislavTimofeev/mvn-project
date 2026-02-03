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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String findById(@PathVariable Long id, Model model) {
        VehicleReadDto vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        return "vehicle/vehicle";
    }

    @GetMapping("/create")
    public String create(Model model, VehicleCreateDto vehicleCreateDto) {
        model.addAttribute("vehicle", vehicleCreateDto);
        return "vehicle/create";
    }

    @PostMapping
    public String save(@ModelAttribute("vehicle") VehicleCreateDto vehicleCreateDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "vehicle/create";
        }
        vehicleService.save(vehicleCreateDto);
        return "redirect:/vehicles";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("vehicle") VehicleEditDto vehicleEditDto) {
        vehicleService.update(id, vehicleEditDto);
        return "redirect:/vehicles/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return "redirect:/vehicles";
    }
}
