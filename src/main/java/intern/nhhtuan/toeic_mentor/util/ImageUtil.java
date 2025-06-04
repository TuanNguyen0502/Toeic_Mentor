package intern.nhhtuan.toeic_mentor.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class ImageUtil {
    private final Cloudinary cloudinary;

    @Autowired
    public ImageUtil(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String saveImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        return uploadResult.get("secure_url").toString(); // Trả về URL ảnh
    }

    public boolean deleteImage(String imageUrl) throws IOException {
        String publicId = extractPublicId(imageUrl);
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result"));
    }

    public boolean isValidSuffixImage(String img) {
        return img.endsWith(".jpg") || img.endsWith(".jpeg") ||
                img.endsWith(".png") || img.endsWith(".gif") ||
                img.endsWith(".bmp");
    }

    public String updateImage(MultipartFile file, String oldImageUrl) throws IOException {
        deleteImage(oldImageUrl);
        return saveImage(file);
    }

    private String extractPublicId(String url) {
        if (url == null || url.isEmpty()) return null;
        // URL: https://res.cloudinary.com/your_cloud/image/upload/v1234567890/filename.jpg
        String[] parts = url.split("/");
        String filename = parts[parts.length - 1]; // filename.jpg
        return filename.split("\\.")[0]; // filename
    }
}
