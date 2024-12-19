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
    public String findById(@PathVariable("id") Long id, Model model) {
        return requestService.findById(id)
                .map(request -> {
                    model.addAttribute("request", request);
                    model.addAttribute("status", RequestStatus.values());
                    return "request/request";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/create")
    public String create(Model model, RequestCreateEditDto requestCreateEditDto) {
        model.addAttribute("request", requestCreateEditDto);
        return "request/create";
    }

    @PostMapping
    public String save(@ModelAttribute("request") RequestCreateEditDto requestCreateEditDto,
                       Model model,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "request/create";
        }
        requestService.save(requestCreateEditDto);
        return "redirect:/requests";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute("request") RequestCreateEditDto requestCreateEditDto) {
        return requestService.update(id, requestCreateEditDto)
                .map(it -> "redirect:/requests/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!requestService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/requests";
    }
}
