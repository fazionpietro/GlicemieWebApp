package it.univr.glicemiewebapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/admin")
  public String adminAccess() {
    return "Admin Content.";
  }

  @GetMapping("/medico")
  public String medicoAccess() {
    return "medico Content.";
  }

}
