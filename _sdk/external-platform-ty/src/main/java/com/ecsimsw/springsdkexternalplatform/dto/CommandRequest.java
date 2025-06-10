package com.ecsimsw.springsdkexternalplatform.dto;

import java.util.List;

public record CommandRequest(
    List<Command> commands
){
}