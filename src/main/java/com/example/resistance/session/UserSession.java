package com.example.resistance.session;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.example.resistance.entity.Member;
import com.example.resistance.entity.Room;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Component
@Getter
@Setter
@Scope(value= "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {

      private static final long serialVersionUID = 1L;

      private String userName;
      private Member member;
      private Room room;
}
