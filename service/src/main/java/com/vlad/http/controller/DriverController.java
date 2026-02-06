package com.vlad.http.controller;

import com.vlad.dto.driver.DriverCreateDto;
import com.vlad.dto.driver.DriverEditDto;
import com.vlad.dto.driver.DriverReadDto;
import com.vlad.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public String findAll(Model model) {
        List<DriverReadDto> drivers = driverService.findAll();
        model.addAttribute("drivers", drivers);
        return "driver/drivers";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        DriverReadDto driver = driverService.findById(id);
        model.addAttribute("driver", driver);
        return "driver/driver";
    }

    @GetMapping("/create")
    public String create(Model model, DriverCreateDto driverCreateDto) {
        model.addAttribute("driver", driverCreateDto);
        return "driver/create";
    }

    @PostMapping
    public String save(@ModelAttribute("driver") DriverCreateDto driverCreateDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "driver/create";
        }
        driverService.save(driverCreateDto);
        return "redirect:/drivers";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("driver") DriverEditDto driverEditDto) {
        driverService.update(id, driverEditDto);
        return "redirect:/drivers/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        driverService.delete(id);
        return "redirect:/drivers";
    }
}
