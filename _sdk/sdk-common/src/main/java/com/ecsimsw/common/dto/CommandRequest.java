package com.ecsimsw.common.dto;

import java.util.List;

public record CommandRequest(
    List<Command> commands
){
}