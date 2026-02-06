package com.vlad.http.controller;

import com.vlad.dto.PageResponse;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.dto.request.RequestReadDto;
import com.vlad.entity.RequestStatus;
import com.vlad.service.RequestService;
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
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public String findAll(Model model, RequestFilterDto requestFilterDto, Pageable pageable) {
        Page<RequestReadDto> page = requestService.findAll(requestFilterDto, pageable);
        model.addAttribute("requests", PageResponse.of(page));
        model.addAttribute("filter", requestFilterDto);
        return "request/requests";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        RequestReadDto request = requestService.findById(id);
        model.addAttribute("request", request);
        model.addAttribute("status", RequestStatus.values());
        return "request/request";
    }

    @GetMapping("/create")
    public String create(Model model, RequestCreateEditDto requestCreateEditDto) {
        model.addAttribute("request", requestCreateEditDto);
        return "request/create";
    }

    @PostMapping
    public String save(@ModelAttribute("request") RequestCreateEditDto requestCreateEditDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "request/create";
        }
        requestService.save(requestCreateEditDto);
        return "redirect:/requests";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("request") RequestCreateEditDto requestCreateEditDto) {
        requestService.update(id, requestCreateEditDto);
        return "redirect:/requests/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        requestService.delete(id);
        return "redirect:/requests";
    }
}
