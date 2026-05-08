package com.quine.backend.config;

import com.quine.backend.model.*;
import com.quine.backend.repository.*;
import com.quine.backend.service.EmployeeService;
import com.quine.backend.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    // ── Repositories ──────────────────────────────────────────────────────────
    private final UserRepository           userRepository;
    private final ProgramRepository        programRepository;
    private final SemesterRepository       semesterRepository;
    private final SubjectRepository        subjectRepository;
    private final EmployeeRepository       employeeRepository;
    private final StudentRepository        studentRepository;
    private final MentorRepository         mentorRepository;
    private final ExamResultRepository     examResultRepository;
    private final NoticeRepository         noticeRepository;
    private final NoticeArchiveRepository  noticeArchiveRepository;
    private final ApplicationRepository   applicationRepository;

    // ── Services ──────────────────────────────────────────────────────────────
    private final EmployeeService employeeService;
    private final StudentService  studentService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            ProgramRepository programRepository,
            SemesterRepository semesterRepository,
            SubjectRepository subjectRepository,
            EmployeeRepository employeeRepository,
            StudentRepository studentRepository,
            MentorRepository mentorRepository,
            ExamResultRepository examResultRepository,
            NoticeRepository noticeRepository,
            NoticeArchiveRepository noticeArchiveRepository,
            ApplicationRepository applicationRepository,
            EmployeeService employeeService,
            StudentService studentService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository          = userRepository;
        this.programRepository       = programRepository;
        this.semesterRepository      = semesterRepository;
        this.subjectRepository       = subjectRepository;
        this.employeeRepository      = employeeRepository;
        this.studentRepository       = studentRepository;
        this.mentorRepository        = mentorRepository;
        this.examResultRepository    = examResultRepository;
        this.noticeRepository        = noticeRepository;
        this.noticeArchiveRepository = noticeArchiveRepository;
        this.applicationRepository   = applicationRepository;
        this.employeeService         = employeeService;
        this.studentService          = studentService;
        this.passwordEncoder         = passwordEncoder;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Subject data — 5 subjects × 6 semesters
    // Format: { subjectName, subjectCode, type }
    // ══════════════════════════════════════════════════════════════════════════
    private static final String[][][] SEMESTER_SUBJECTS = {
            // Sem 1
            {
                    {"Introduction to Programming",   "BCA101", "Major"},
                    {"Mathematics I",                 "BCA102", "Major"},
                    {"Computer Organization",         "BCA103", "Major"},
                    {"English Communication",         "BCA104", "Minor"},
                    {"Environmental Science",         "BCA105", "Open Elective"}
            },
            // Sem 2
            {
                    {"Data Structures",               "BCA201", "Major"},
                    {"Mathematics II",                "BCA202", "Major"},
                    {"Operating Systems",             "BCA203", "Major"},
                    {"Technical Writing",             "BCA204", "Minor"},
                    {"Constitution of India",         "BCA205", "Open Elective"}
            },
            // Sem 3
            {
                    {"Database Management Systems",   "BCA301", "Major"},
                    {"Object Oriented Programming",   "BCA302", "Major"},
                    {"Computer Networks",             "BCA303", "Major"},
                    {"Business Communication",        "BCA304", "Minor"},
                    {"Numerical Methods",             "BCA305", "Open Elective"}
            },
            // Sem 4
            {
                    {"Java Programming",              "BCA401", "Major"},
                    {"Software Engineering",          "BCA402", "Major"},
                    {"Web Technologies",              "BCA403", "Major"},
                    {"Statistics for CS",             "BCA404", "Minor"},
                    {"Cyber Security Basics",         "BCA405", "Open Elective"}
            },
            // Sem 5
            {
                    {"Advanced Java & Spring Boot",   "BCA501", "Major"},
                    {"Cloud Computing",               "BCA502", "Major"},
                    {"Mobile App Development",        "BCA503", "Major"},
                    {"Project Management",            "BCA504", "Minor"},
                    {"Machine Learning Basics",       "BCA505", "Open Elective"}
            },
            // Sem 6
            {
                    {"Final Year Project",            "BCA601", "Major"},
                    {"Distributed Systems",           "BCA602", "Major"},
                    {"DevOps & CI/CD",                "BCA603", "Major"},
                    {"Entrepreneurship",              "BCA604", "Minor"},
                    {"Research Methodology",          "BCA605", "Open Elective"}
            }
    };

    @Override
    public void run(String... args) {

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    QuineInstitute — DB Initializer v2.0    ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        // ══════════════════════════════════════════════════════════════════════
        // [1] ADMIN USER
        // ══════════════════════════════════════════════════════════════════════
        if (userRepository.findByUsername("Ryu-Admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("Ryu-Admin");
            admin.setPassword(passwordEncoder.encode("ryu11"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ [1] Admin user → Ryu-Admin / ryu11");
        } else {
            System.out.println("🚀 [1] Admin already in DB.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // [2] PROGRAMS  (BCA + MCA)
        // ══════════════════════════════════════════════════════════════════════
        Program bca = programRepository.findByName("BCA").orElseGet(() -> {
            Program p = new Program();
            p.setName("BCA");
            p.setFullName("Bachelor of Computer Applications");
            p.setCapacity(240);
            p.setDurationYears(3);
            p.setAnnualFee(50000.0);
            p.setTotalSemesters(6);
            return programRepository.save(p);
        });

        programRepository.findByName("MCA").orElseGet(() -> {
            Program p = new Program();
            p.setName("MCA");
            p.setFullName("Master of Computer Applications");
            p.setCapacity(60);
            p.setDurationYears(2);
            p.setAnnualFee(75000.0);
            p.setTotalSemesters(4);
            return programRepository.save(p);
        });

        System.out.println("✅ [2] Programs → BCA + MCA");

        // ══════════════════════════════════════════════════════════════════════
        // [3] EMPLOYEES  (EmployeeService handles EMP-ID + User auto-creation)
        // ══════════════════════════════════════════════════════════════════════
        Employee teacher = null;

        if (employeeRepository.count() == 0) {

            // EMP001 — Teacher (FACULTY role)
            Employee e1 = new Employee();
            e1.setName("Arjun Sharma");
            e1.setEmail("arjun.sharma@quine.edu");
            e1.setMobileNumber("9876543210");
            e1.setSalary(55000.0);
            e1.setProfession("TEACHER");
            e1.setDepartment("COMPUTER SCIENCE");
            e1.setAssignProgram("BCA");
            e1.setAssignSemester("1");
            e1.setAssignSection("ALPHA");
            teacher = employeeService.addEmployee(e1);

            // EMP002 — Examiner (EXAMINER role)
            Employee e2 = new Employee();
            e2.setName("Priya Verma");
            e2.setEmail("priya.verma@quine.edu");
            e2.setMobileNumber("9876543211");
            e2.setSalary(48000.0);
            e2.setProfession("EXAMINER");
            e2.setDepartment("EXAM DEPARTMENT");
            e2.setAssignProgram("BCA");
            e2.setAssignSemester("1");
            e2.setAssignSection("ALPHA");
            employeeService.addEmployee(e2);

            // EMP003 — Staff (STAFF role)
            Employee e3 = new Employee();
            e3.setName("Ramesh Patel");
            e3.setEmail("ramesh.patel@quine.edu");
            e3.setMobileNumber("9876543212");
            e3.setSalary(30000.0);
            e3.setProfession("STAFF");
            e3.setDepartment("ADMINISTRATION");
            e3.setAssignProgram("");
            e3.setAssignSemester("");
            e3.setAssignSection("");
            employeeService.addEmployee(e3);

            // EMP004 — Second Teacher
            Employee e4 = new Employee();
            e4.setName("Sonal Joshi");
            e4.setEmail("sonal.joshi@quine.edu");
            e4.setMobileNumber("9876543213");
            e4.setSalary(52000.0);
            e4.setProfession("TEACHER");
            e4.setDepartment("COMPUTER SCIENCE");
            e4.setAssignProgram("BCA");
            e4.setAssignSemester("2");
            e4.setAssignSection("BETA");
            employeeService.addEmployee(e4);

            System.out.println("✅ [3] 4 Employees (EMP001–EMP004) + their Users auto-created");
            System.out.println("       Login: EMP00x / <mobile number>");

        } else {
            teacher = employeeRepository.findByEmployeeId("EMP001");
            System.out.println("🚀 [3] Employees already in DB.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // [4] SEMESTERS  (6 for BCA)
        // ══════════════════════════════════════════════════════════════════════
        Semester[] semesters = new Semester[6];
        for (int i = 1; i <= 6; i++) {
            final int semNum = i;
            final Program prog = bca;
            semesters[i - 1] = semesterRepository.findByProgramAndSemNumber(bca, i)
                    .orElseGet(() -> {
                        Semester s = new Semester();
                        s.setSemNumber(semNum);
                        s.setProgram(prog);
                        return semesterRepository.save(s);
                    });
        }
        Semester sem1 = semesters[0];
        System.out.println("✅ [4] 6 Semesters ready for BCA");

        // ══════════════════════════════════════════════════════════════════════
        // [5] SUBJECTS  (5 per semester × 6 semesters = 30 total)
        // ══════════════════════════════════════════════════════════════════════
        int subjectsSeeded = 0;
        for (int i = 0; i < 6; i++) {
            Semester sem = semesters[i];
            if (subjectRepository.findBySemester(sem).isEmpty()) {
                for (String[] sd : SEMESTER_SUBJECTS[i]) {
                    Subject sub = new Subject();
                    sub.setSubjectName(sd[0]);
                    sub.setSubjectCode(sd[1]);
                    sub.setType(sd[2]);
                    sub.setSemester(sem);
                    subjectRepository.save(sub);
                    subjectsSeeded++;
                }
            }
        }
        System.out.println(subjectsSeeded > 0
                ? "✅ [5] " + subjectsSeeded + " Subjects seeded (BCA101–BCA605)"
                : "🚀 [5] Subjects already in DB.");

        // ══════════════════════════════════════════════════════════════════════
        // [6] STUDENTS  (StudentService handles ID + User auto-creation)
        // ══════════════════════════════════════════════════════════════════════
        Student student1 = null;

        if (studentRepository.count() == 0) {

            // Student 1 — Sem 1, ALPHA, partial fees
            Student s1 = new Student();
            s1.setName("Rahul Kumar");
            s1.setFatherName("Suresh Kumar");
            s1.setMotherName("Sunita Kumar");
            s1.setEmail("rahul.kumar@student.quine.edu");
            s1.setMobileNumber("9123456789");
            s1.setSemester(semesters[0]);
            s1.setAssignedSection(SectionEnum.ALPHA);
            s1.setTotalFees(50000.0);
            s1.setFeesPaid(25000.0);
            s1.setJoiningDate(LocalDateTime.now().minusMonths(4));
            student1 = studentService.saveStudent(s1); // → 100001

            // Student 2 — Sem 3, BETA, fully paid
            Student s2 = new Student();
            s2.setName("Ananya Singh");
            s2.setFatherName("Rajesh Singh");
            s2.setMotherName("Kavita Singh");
            s2.setEmail("ananya.singh@student.quine.edu");
            s2.setMobileNumber("9123456780");
            s2.setSemester(semesters[2]);
            s2.setAssignedSection(SectionEnum.BETA);
            s2.setTotalFees(50000.0);
            s2.setFeesPaid(50000.0);
            s2.setJoiningDate(LocalDateTime.now().minusYears(1));
            studentService.saveStudent(s2); // → 100002

            System.out.println("✅ [6] 2 Students → 100001 (Rahul, Sem1/ALPHA) | 100002 (Ananya, Sem3/BETA)");
            System.out.println("       Login: <studentId> / <mobile number>");

        } else {
            student1 = studentRepository.findByStudentId("100001").orElse(null);
            System.out.println("🚀 [6] Students already in DB.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // [7] MENTOR
        // ══════════════════════════════════════════════════════════════════════
        if (teacher != null && mentorRepository.findBySemesterAndSection(sem1, SectionEnum.ALPHA).isEmpty()) {
            Mentor mentor = new Mentor();
            mentor.setEmployee(teacher);
            mentor.setSemester(sem1);
            mentor.setSection(SectionEnum.ALPHA);
            mentorRepository.save(mentor);
            System.out.println("✅ [7] Mentor → EMP001 (Arjun Sharma) → Sem1 / ALPHA");
        } else {
            System.out.println("🚀 [7] Mentor already assigned.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // [8] EXAM RESULTS  (student1 ke Sem1 ke saare 5 subjects)
        // ══════════════════════════════════════════════════════════════════════
        if (student1 != null && examResultRepository.findByStudentId(student1.getId()).isEmpty()) {
            List<Subject> sem1Subjects = subjectRepository.findBySemester(sem1);

            // { internalMarks, externalMarks, attempt }
            int[][] marksData = {
                    {28, 65, 1},   // BCA101 — total 93 → PASS
                    {25, 58, 1},   // BCA102 — total 83 → PASS
                    {20, 40, 1},   // BCA103 — total 60 → PASS
                    {30, 70, 1},   // BCA104 — total 100 → PASS
                    {10, 15, 1}    // BCA105 — total 25 → FAIL
            };

            for (int i = 0; i < sem1Subjects.size() && i < marksData.length; i++) {
                ExamResult result = new ExamResult();
                result.setStudent(student1);
                result.setSubject(sem1Subjects.get(i));
                result.setInternalMarks(marksData[i][0]);
                result.setExternalMarks(marksData[i][1]);
                result.setAttempt(marksData[i][2]);
                result.setStatus((marksData[i][0] + marksData[i][1]) >= 33 ? "PASS" : "FAIL");
                examResultRepository.save(result);
            }
            System.out.println("✅ [8] " + Math.min(sem1Subjects.size(), marksData.length)
                    + " ExamResults seeded for Rahul Kumar (Sem 1, all subjects)");
        } else {
            System.out.println("🚀 [8] Exam results already in DB.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // [9] NOTICES  (3 active notices)
        // ══════════════════════════════════════════════════════════════════════
        if (noticeRepository.count() == 0) {

            Notice n1 = new Notice();
            n1.setTitle("Welcome to Quine Institute — Session 2024-25");
            n1.setContent("Dear students, welcome to the new academic session 2024-25. " +
                    "Classes for Semester 1 begin from Monday. " +
                    "All students must report to their sections by 9:00 AM.");
            n1.setType("General");
            n1.setDate(LocalDate.now());
            n1.setDateOfExecution(LocalDate.now().plusDays(7));
            n1.setNew(true);
            noticeRepository.save(n1);

            Notice n2 = new Notice();
            n2.setTitle("Mid-Semester Examination Schedule — Sem 1");
            n2.setContent("Mid-semester examinations for Semester 1 will commence from " +
                    LocalDate.now().plusDays(15) + ". " +
                    "Hall tickets will be distributed 3 days prior.");
            n2.setType("Examination");
            n2.setDate(LocalDate.now());
            n2.setDateOfExecution(LocalDate.now().plusDays(15));
            n2.setNew(true);
            noticeRepository.save(n2);

            Notice n3 = new Notice();
            n3.setTitle("Annual Tech Fest — CodeStorm 2024");
            n3.setContent("Quine Institute announces CodeStorm 2024. " +
                    "Events: hackathons, coding contests, and guest lectures. " +
                    "Register on the institute portal before the deadline.");
            n3.setType("Event");
            n3.setDate(LocalDate.now().minusDays(2));
            n3.setDateOfExecution(LocalDate.now().plusDays(20));
            n3.setNew(false);
            noticeRepository.save(n3);

            System.out.println("✅ [9] 3 Notices seeded (General + Examination + Event)");
        } else {
            System.out.println("🚀 [9] Notices already in DB.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // [10] NOTICE ARCHIVE
        // ══════════════════════════════════════════════════════════════════════
        if (noticeArchiveRepository.count() == 0) {

            NoticeArchive a1 = new NoticeArchive();
            a1.setTitle("End Semester Exam Results Declared — 2023-24");
            a1.setContent("Results for end semester exams 2023-24 have been declared. " +
                    "Students can check results on the institute portal.");
            a1.setType("Examination");
            a1.setDate(LocalDate.of(2024, 1, 10));
            a1.setDateOfExecution(LocalDate.of(2024, 1, 15));
            noticeArchiveRepository.save(a1);

            NoticeArchive a2 = new NoticeArchive();
            a2.setTitle("Sports Day 2023 — Winners List");
            a2.setContent("Annual Sports Day 2023 was successfully conducted. " +
                    "Winners may collect their certificates from the admin office.");
            a2.setType("Event");
            a2.setDate(LocalDate.of(2023, 12, 5));
            a2.setDateOfExecution(LocalDate.of(2023, 12, 5));
            noticeArchiveRepository.save(a2);

            System.out.println("✅ [10] 2 NoticeArchives seeded");
        } else {
            System.out.println("🚀 [10] NoticeArchives already in DB.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // [11] APPLICATIONS  (PENDING + APPROVED + REJECTED)
        // ══════════════════════════════════════════════════════════════════════
        if (applicationRepository.count() == 0) {

            Application app1 = new Application();
            app1.setName("Vikram Yadav");
            app1.setFatherName("Mohan Yadav");
            app1.setMotherName("Rekha Yadav");
            app1.setEmail("vikram.yadav@gmail.com");
            app1.setMobileNumber("9988776655");
            app1.setProgram(bca);
            app1.setPercentage(78.5);
            app1.setStatus("PENDING");
            applicationRepository.save(app1);

            Application app2 = new Application();
            app2.setName("Ananya Singh");
            app2.setFatherName("Rajesh Singh");
            app2.setMotherName("Kavita Singh");
            app2.setEmail("ananya.singh@gmail.com");
            app2.setMobileNumber("9123456780");
            app2.setProgram(bca);
            app2.setPercentage(91.0);
            app2.setStatus("APPROVED");
            applicationRepository.save(app2);

            Application app3 = new Application();
            app3.setName("Rohit Mishra");
            app3.setFatherName("Anil Mishra");
            app3.setMotherName("Geeta Mishra");
            app3.setEmail("rohit.mishra@gmail.com");
            app3.setMobileNumber("9871234567");
            app3.setProgram(bca);
            app3.setPercentage(42.0);
            app3.setStatus("REJECTED");
            applicationRepository.save(app3);

            System.out.println("✅ [11] 3 Applications seeded (PENDING + APPROVED + REJECTED)");
        } else {
            System.out.println("🚀 [11] Applications already in DB.");
        }

        // ══════════════════════════════════════════════════════════════════════
        // SUMMARY
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║   🎯 QuineInstitute — All systems go!                   ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  ROLE       USERNAME        PASSWORD                    ║");
        System.out.println("║  ─────────  ─────────────  ─────────────────────────   ║");
        System.out.println("║  ADMIN      Ryu-Admin       ryu11                       ║");
        System.out.println("║  FACULTY    EMP001          9876543210  (Arjun)         ║");
        System.out.println("║  EXAMINER   EMP002          9876543211  (Priya)         ║");
        System.out.println("║  STAFF      EMP003          9876543212  (Ramesh)        ║");
        System.out.println("║  FACULTY    EMP004          9876543213  (Sonal)         ║");
        System.out.println("║  STUDENT    100001          9123456789  (Rahul)         ║");
        System.out.println("║  STUDENT    100002          9123456780  (Ananya)        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
    }
}