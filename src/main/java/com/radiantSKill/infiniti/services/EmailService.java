package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.ThesisRegistration;
import com.radiantSKill.infiniti.repository.ThesisRegistrationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final ThesisRegistrationRepository thesisRegistrationRepository;

    ThesisRegistration ensureRegistrationCompleted(AppUser student) {
        return thesisRegistrationRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Thesis Registration not completed"));
    }
    public void sendMail(String to, String subject, String message){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("Kalpesh@radiantskill.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);

        javaMailSender.send(mail);

    }
    public void sendMilestoneEmail(AppUser student, String milestoneName) {

        ThesisRegistration reg = ensureRegistrationCompleted(student);

        String studentSubject = "Milestone Completed - " + milestoneName;

        String studentBody =
                "Dear Student,\n\n" +
                        "Congratulations! 🎉\n\n" +
                        "You have successfully completed the \"" + milestoneName + "\" milestone of your thesis project.\n" +
                        "This is an important step in your academic journey and reflects your dedication and effort.\n\n" +
                        "Keep working with the same enthusiasm as you move forward to the next milestones.\n\n" +
                        "Best regards,\n" +
                        "Team Infiniti";

        // 3. Parent Email Content
        String parentSubject = "Milestone Update - " + milestoneName + " Completed";

        String parentBody =
                "Dear Parent,\n\n" +
                        "We are pleased to inform you that your child has successfully completed the \"" + milestoneName + "\" milestone.\n\n" +
                        "This achievement marks steady progress in their thesis journey and showcases their commitment.\n\n" +
                        "We appreciate your continued support and encouragement in helping them reach their goals.\n\n" +
                        "Best regards,\n" +
                        "Team Infiniti";

        sendMail(reg.getStudentEmail(), studentSubject, studentBody);

        if (reg.getParentEmail() != null) {
            sendMail(reg.getParentEmail(), parentSubject, parentBody);
        }
    }
    public void sendRegistrationEmail(AppUser student) {

        ThesisRegistration reg = ensureRegistrationCompleted(student);

        // Student Email
        String studentSubject = "Thesis Registration Successful";

        String studentBody = """
            Dear Student,

            Congratulations! 🎉

            Your thesis registration has been completed successfully.
            You are now officially enrolled for the thesis project process.

            This marks the beginning of an important academic journey.
            Please stay updated with upcoming milestones, reviews,
            and submission schedules shared by your guide.

            We wish you success and a great learning experience.

            Best regards,
            Team Infiniti
            """;

        // Parent Email
        String parentSubject = "Student Thesis Registration Successful";

        String parentBody = """
            Dear Parent,

            We are pleased to inform you that your child has
            successfully completed the thesis registration process.

            They are now officially enrolled for the thesis project journey.
            This achievement is an important step in their academics.

            Your continued support and encouragement play
            a valuable role in their success and growth.

            Thank you for being a part of their journey.

            Best regards,
            Team Infiniti
            """;

        // Send mail to student
        sendMail(reg.getStudentEmail(), studentSubject, studentBody);

        // Send mail to parent if available
        if (reg.getParentEmail() != null && !reg.getParentEmail().isBlank()) {
            sendMail(reg.getParentEmail(), parentSubject, parentBody);
        }
    }
}
