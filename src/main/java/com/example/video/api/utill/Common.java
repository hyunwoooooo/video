package com.example.video.api.utill;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
public class Common {
    public class StatusCode {
        public static final int RETURN_SUCCESS = 1000;
        public static final int RETURN_NOTOKEN = 9998;
        public static final int RETURN_ERROR = 9999;
    }

    public static String asteriskGroup = "asterisk";
    public static String asteriskUser = "asterisk";

    public static String asteriskConfDir() {
        return "/etc/asterisk/";
    }

    public static String asteriskMohDir() {
        return "/var/lib/asterisk/moh/";
    }

    public static String systemMonDir() {
        return "/data/system/";
    }

    public static String recordDataDir() {
        return "/data/record/";
    }

    public static String getMD5(String str) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }

    public static int setUserAndGroup(String filepath,String user,String group) {
        try {
            Path path = Paths.get(filepath);
            FileOwnerAttributeView view = Files.getFileAttributeView(path,
                    FileOwnerAttributeView.class);
            UserPrincipalLookupService lookupService = FileSystems.getDefault()
                    .getUserPrincipalLookupService();
            UserPrincipal userPrincipal = lookupService.lookupPrincipalByName(user);

            Files.setOwner(path, userPrincipal);
            log.debug("Owner: " + view.getOwner().getName());

            GroupPrincipal GroupP = lookupService.lookupPrincipalByGroupName(group);
            Files.getFileAttributeView(path, PosixFileAttributeView.class,
                    LinkOption.NOFOLLOW_LINKS).setGroup(GroupP);
        }
        catch (Exception e) {
        }

        return 1;
    }

    public static String passEncrypy(String password) {

        try {
            TrippleDes td = new TrippleDes();
            String encrypted = td.encrypt(password);
            encrypted = td.encrypt(encrypted);

            return encrypted;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String passDecrypy(String encrypted) {

        try {
            TrippleDes td = new TrippleDes();
            String decrypted = td.decrypt(encrypted);
            decrypted = td.decrypt(decrypted);

            return decrypted;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public static String getuserRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getHeader("Proxy-Client-IP");
        if (ip == null) ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        if (ip == null) ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null) ip = request.getRemoteAddr();

        return ip;
    }



    private static String encryptStringPrivate(String ciphertext) throws Exception {
        String key = "50c95E7A3180C9Fd4eD8b85c1831E854";
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(ciphertext.getBytes("UTF-8"));

        return new String(Base64.encodeBase64(encrypted));
    }

    public static String encryptString(String data) {
        try {
            return encryptStringPrivate(data);
        }
        catch(Exception e) {
            log.debug(e.getMessage());
            return "";
        }
    }


    private static String decryptStringPrivate(String encrypttext) throws Exception {
        String key = "50c95E7A3180C9Fd4eD8b85c1831E854";
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = Base64.decodeBase64(encrypttext.getBytes("UTF-8"));

        return new String(cipher.doFinal(decrypted), "UTF-8");
    }

    public static String decryptString(String data) {
        try {
            return decryptStringPrivate(data);
        }
        catch(Exception e) {
            log.debug(e.getMessage());
            return "";
        }
    }

    public void fileStream(String filePath, HttpServletResponse res) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            res.setContentType(mimeType);
            res.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

            res.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, res.getOutputStream());
        }
        else {
            log.error("Not found Intro File : " + filePath);
            res.sendError(404, "not found");
        }
    }

    public static String fileUpload(String filePath, String fileName, MultipartFile multipartFile) {
        String saveFileName = fileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        File targetFile = new File(filePath + "/" + saveFileName);
        targetFile.deleteOnExit();

        try {
            InputStream is = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(is, targetFile);

            return saveFileName;

//            String fileContent = "";
//            FileReader filereader = new FileReader(targetFile);
//            int offset = 0;
//            char []cbuf = new char[1024*10];
//
//            while(true) {
//                int res = filereader.read(cbuf,0, 1024*10);
//                if(res ==-1)
//                    break;
//                offset+=res;
//                fileContent+=new String(cbuf);
//            }
        }
        catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);
            throw new RuntimeException(e.getMessage());
        }
    }



}