package com.tekup.miniproject.web.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e,
                                                            Model model){
        return "<strong>error: </strong>"+e.getMessage();
       //return "errors";
    } 
   /*  @ExceptionHandler(Exception.class)
    //@ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String ExceptionHandler(Exception e,
                                                            Model model){
      //  return "<strong>error: </strong>"+e.getMessage();
      model.addAttribute("error", e.getMessage());
       return "errors";
    }  */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    //@ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e,
                                                            Model model){
        //return "<strong>error: </strong>"+e.getMessage();
        model.addAttribute("error", e.getMessage());
       return "errors";
    } 
}