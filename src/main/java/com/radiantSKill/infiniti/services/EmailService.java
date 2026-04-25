package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.ThesisRegistration;
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
    private final ThesisService thesisService;

    public void sendMail(String to, String subject, String message){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("Kalpesh@radiantskill.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);

        javaMailSender.send(mail);

    }
    public void sendMilestoneEmail(AppUser student, String milestoneName) {

        ThesisRegistration reg = thesisService.ensureRegistrationCompleted(student);

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
}
