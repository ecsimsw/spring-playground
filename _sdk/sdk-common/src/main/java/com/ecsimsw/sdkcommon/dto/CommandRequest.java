package com.ecsimsw.sdkcommon.dto;

import java.util.List;

public record CommandRequest(
    List<Command> commands
){
}