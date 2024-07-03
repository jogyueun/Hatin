package com.project1.hatin.routine.controller

import com.project1.hatin.common.dto.BaseResponse
import com.project1.hatin.common.dto.CustomUser
import com.project1.hatin.routine.dto.RoutineRequestDTO.CreateRequestDTO
import com.project1.hatin.routine.dto.RoutineRequestDTO.PatchRequestDTO
import com.project1.hatin.routine.dto.RoutineResponseDTO.CreateResponseDTO
import com.project1.hatin.routine.dto.RoutineResponseDTO.PatchResponseDTO
import com.project1.hatin.routine.dto.RoutineResponseDTO.ShowResponseDTO
import com.project1.hatin.routine.service.RoutineService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@Tag(name = "루틴 Api 컨트롤러", description = "루틴생성, 조회, 수정, 삭제 Api 명세서입니다.")
@RestController
@RequestMapping("/api/routine")
class RoutineController(
     private val routineService: RoutineService
){

    @Operation(summary = "사용자 루틴 전체 조회", description = "사용자 루틴 전체 조회")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    private fun showAllRoutine(@Parameter(description = "헤더에 담긴 유저정보") @AuthenticationPrincipal userInfo: CustomUser)
            : ResponseEntity<Any>{
        val result = routineService.showRoutineList(userInfo)
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse(data = result))
    }

    @Operation(summary = "사용자 루틴 생성", description = "사용자 루틴 생성")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    private fun postRoutine(@Parameter(description = "사용자가 생성하는 루틴 데이터") @Valid @RequestBody createRequestDTO: CreateRequestDTO,
                            @Parameter(description = "헤더에 담긴 유저정보") @AuthenticationPrincipal userInfo: CustomUser)
     : ResponseEntity<BaseResponse<CreateResponseDTO>> {
        val result = routineService.createRoutine(createRequestDTO,userInfo)
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse(data = result))
    }

    @Operation(summary = "사용자 루틴 수정", description = "사용자 루틴 수정")
    @PatchMapping("/{id}")
    private fun patchRoutine(@Parameter(required = true,description = "루틴 ID") @PathVariable(name = "id") id : Long,
                             @Parameter(description = "사용자가 수정하는 루틴 데이터") @Valid @RequestBody patchRequestDTO: PatchRequestDTO )
    : ResponseEntity<BaseResponse<PatchResponseDTO>> {
        val result = routineService.patchRoutine(id,patchRequestDTO)
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse(data = result))
    }

    @Operation(summary = "사용자 루틴 삭제", description = "사용자 루틴 삭제")
    @DeleteMapping("/{id}")
    private fun deleteRoutine(@Parameter(required = true,description = "루틴 ID") @PathVariable(name = "id") id : Long)
            : ResponseEntity<BaseResponse<Any>> {
        routineService.deleteRoutine(id)
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse(data = null))
    }
}