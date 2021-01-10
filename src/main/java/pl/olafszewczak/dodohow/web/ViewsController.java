package pl.olafszewczak.dodohow.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewsController {

    @GetMapping("/")
    public String getHome() {
        return "home/index";
    }

    @GetMapping("/sections")
    public ResponseEntity<HttpHeaders> getSections() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "localhost:3000/MainPage");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
