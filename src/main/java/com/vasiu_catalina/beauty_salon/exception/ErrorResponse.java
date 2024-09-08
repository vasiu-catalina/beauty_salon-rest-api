package com.vasiu_catalina.beauty_salon.exception;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
      private Map<String, String> errors;
}
