package com.ecsimsw.springexternalplatform2.dto;

import java.util.List;

public record CommandRequest(
    List<Command> commands
){
}