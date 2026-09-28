package com.example.springfw01;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public Mem getMem() {
        Mem m = new Mem();
        return m;
    }
}
