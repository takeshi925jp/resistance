package com.example.resistance.handler;

import java.io.Serializable;

import lombok.Data;

/**
 * リクエストボディにセットするエラー情報
 */
@Data
public class ApiError implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
}
