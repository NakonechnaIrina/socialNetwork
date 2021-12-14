package edu.mum.ea.socialnetwork.services.impl;

import edu.mum.ea.socialnetwork.domain.Profile;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.repository.ProfileRepository;
import edu.mum.ea.socialnetwork.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private JavaMailSender javaMailSender;
    private ProfileRepository profileRepository;
    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, ProfileRepository profileRepository) {
        this.javaMailSender = javaMailSender;
        this.profileRepository = profileRepository;
    }

    @Override
    public void sendEmail(User user, String subject, String body) {
        SimpleMailMessage email = new SimpleMailMessage();
        Profile profile = profileRepository.getByUserId(user.getId());
        email.setTo(profile.getEmail());
        email.setFrom("asal.sn.2019@gmail.com");
        email.setSubject(subject);
        email.setText(body);
        try {
            javaMailSender.send(email);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
