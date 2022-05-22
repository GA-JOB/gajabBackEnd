package com.gajob.service.user;

import com.gajob.entity.user.User;
import com.gajob.repository.user.UserRepository;
import com.gajob.util.SecurityUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProfileImageServiceImpl implements ProfileImageService {

  private final UserRepository userRepository;

  @Value("${profileImg.path}")
  private String uploadFolder;

  // 프로필 사진 업로드
  @Transactional
  public void upload(MultipartFile multipartFile) {
    User user = userRepository.findOneWithAuthoritiesByEmail(
        SecurityUtil.getCurrentUsername().get()).get();

    if (!multipartFile.isEmpty()) {
      String imageFileName = user.getId() + "_" + multipartFile.getOriginalFilename();
      Path imageFilePath = Paths.get(uploadFolder + imageFileName);
      try {
        // 프로필 사진이 존재할 경우, 기존의 파일은 삭제
        if (user.getProfileImg() != null) {
          File file = new File(uploadFolder + user.getProfileImg());
          file.delete();
        }
        Files.write(imageFilePath, multipartFile.getBytes());
      } catch (Exception e) {
        e.printStackTrace();
      }
      user.profileImgUpdate(imageFileName);
    }
  }

  // 프로필 사진 조회
  @Transactional
  public byte[] getProfile() throws IOException {
    User user = userRepository.findOneWithAuthoritiesByEmail(
        SecurityUtil.getCurrentUsername().get()).get();

    // 유저의 프로필 사진의 경로를 찾아서 조회
    InputStream imageStram = new FileInputStream(uploadFolder + user.getProfileImg());

    byte[] imageByteArray = IOUtils.toByteArray(imageStram);
    imageStram.close();

    return imageByteArray;

  }

  // 프로필 사진 삭제
  @Transactional
  public void delete() {
    User user = userRepository.findOneWithAuthoritiesByEmail(
        SecurityUtil.getCurrentUsername().get()).get();

    // 프로필 사진이 존재할 경우, file을 먼저 삭제 후 user의 프로필 데이터를 null 로 변경
    if (!(user.getProfileImg() == null)) {
      File file = new File(uploadFolder + user.getProfileImg());
      file.delete();
      user.setProfileImg(null);
    }
  }
}


