package com.stu.attendance.controller;

import com.stu.attendance.dto.AttendanceReportDto;
import com.stu.attendance.dto.AttendanceRequest;
import com.stu.attendance.dto.AttendanceResponse;
import com.stu.attendance.dto.SessionDto;
import com.stu.attendance.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")//danh cho student
@RequiredArgsConstructor
//@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    // Sessions and Attendance
//    @GetMapping("/sessions/today")
//    public ResponseEntity<List<SessionDto>> getTodaySessions() {
//        return ResponseEntity.ok(studentService.getTodaySessions());
//    }

//    @GetMapping("/sessions/upcoming")
//    public ResponseEntity<List<SessionDto>> getUpcomingSessions(
//            @RequestParam(defaultValue = "7") Integer days) {
//        return ResponseEntity.ok(studentService.getUpcomingSessions(days));
//    }

    @GetMapping("/attendance")//lay danh sach diem danh
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String subjectId) {
        return ResponseEntity.ok(studentService.getMyAttendance(startDate, endDate, subjectId));
    }

    @GetMapping("/attendance/today")//lay danh sach diem danh
    public ResponseEntity<List<AttendanceResponse>> getTodayAttendance() {
        return ResponseEntity.ok(studentService.getTodayAttendance());
    }

    @PostMapping("/attendance/qr")//lay danh sach diem danh
    public ResponseEntity<AttendanceResponse> attendViaQrCode(@RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(studentService.attendViaQrCode(request.getQrToken()));
    }


    // Schedule
    @GetMapping("/schedule")//lay danh sach lich hoc
    public ResponseEntity<List<Map<String, Object>>> getWeeklySchedule(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(studentService.getWeeklySchedule(startDate));
    }

    // Subjects
    @GetMapping("/subjects")//lay danh sach mon
    public ResponseEntity<List<Map<String, Object>>> getMySubjects() {
        return ResponseEntity.ok(studentService.getMySubjects());
    }

    @GetMapping("/subjects/{subjectId}/sessions")//lay danh sach lich hoc
    public ResponseEntity<List<SessionDto>> getSubjectSessions(
            @PathVariable String subjectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(studentService.getSubjectSessions(subjectId, startDate, endDate));
    }
}