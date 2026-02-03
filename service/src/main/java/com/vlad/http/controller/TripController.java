package com.vlad.http.controller;

import com.vlad.dto.PageResponse;
import com.vlad.dto.filter.TripFilterDto;
import com.vlad.dto.trip.TripCreateDto;
import com.vlad.dto.trip.TripEditDto;
import com.vlad.dto.trip.TripReadDto;
import com.vlad.entity.TripStatus;
import com.vlad.service.TripService;
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
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public String findAll(Model model, TripFilterDto tripFilterDto, Pageable pageable) {
        Page<TripReadDto> page = tripService.findAll(tripFilterDto, pageable);
        model.addAttribute("trips", PageResponse.of(page));
        model.addAttribute("filter", tripFilterDto);
        return "trip/trips";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        TripReadDto trip = tripService.findById(id);
        model.addAttribute("trip", trip);
        return "trip/trip";
    }

    @GetMapping("/create")
    public String create(Model model, TripCreateDto tripCreateDto) {
        model.addAttribute("trip", tripCreateDto);
        model.addAttribute("status", TripStatus.PENDING);
        return "trip/create";
    }

    @PostMapping
    public String save(@ModelAttribute("trip") TripCreateDto tripCreateDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "trip/create";
        }
        tripService.save(tripCreateDto);
        return "redirect:/trips";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("trip") TripEditDto tripEditDto) {
        tripService.update(id, tripEditDto);
        return "redirect:/trips/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        tripService.delete(id);
        return "redirect:/trips";
    }
}
