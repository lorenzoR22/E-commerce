package com.example.e_commerce.Shared.Exceptions;

import com.example.e_commerce.Carrito.Exceptions.CarritoNotFoundException;
import com.example.e_commerce.Carrito.Exceptions.ProductoCarritoNotFoundException;
import com.example.e_commerce.Pedido.Exceptions.PedidoNotFoundException;
import com.example.e_commerce.Producto.Exceptions.NoMoreStockException;
import com.example.e_commerce.Producto.Exceptions.ProductoNotFoundException;
import com.example.e_commerce.User.Exceptions.UserNotFoundException;
import com.example.e_commerce.User.Exceptions.UsernameAlreadyExistsException;
import com.mercadopago.exceptions.MPApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>handleGenericException(Exception e){
        ErrorResponse error=new ErrorResponse("INTERNAL_SERVER_ERROR",e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(CarritoNotFoundException.class)
    public ResponseEntity<ErrorResponse>CarritoNotFoundException(CarritoNotFoundException e){
        ErrorResponse error=new ErrorResponse("CARRITO_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse>UserNotFoundException(UserNotFoundException e){
        ErrorResponse error=new ErrorResponse("USER_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorResponse>ProductoNotFoundException(ProductoNotFoundException e){
        ErrorResponse error=new ErrorResponse("PRODUCTO_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProductoCarritoNotFoundException.class)
    public ResponseEntity<ErrorResponse>ProductoCarritoNotFoundException(ProductoCarritoNotFoundException e){
        ErrorResponse error=new ErrorResponse("PRODUCTO_CARRITO_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<ErrorResponse>PedidoNotFoundException(PedidoNotFoundException e){
        ErrorResponse error=new ErrorResponse("PEDIDO_NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NoMoreStockException.class)
    public ResponseEntity<ErrorResponse>noMoreStockException(NoMoreStockException e){
        ErrorResponse error=new ErrorResponse("NO_MORE_STOCK",e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse>usernameAlreadyExistsException(UsernameAlreadyExistsException e){
        ErrorResponse error=new ErrorResponse("USER_ALREADY_EXISTS",e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MPApiException.class)
    public ResponseEntity<ErrorResponse>handleMPApiException(MPApiException e){
        ErrorResponse error=new ErrorResponse("MP_ERROR",e.getApiResponse().getContent());
        return new ResponseEntity<>(error,HttpStatus.BAD_GATEWAY);
    }
}
