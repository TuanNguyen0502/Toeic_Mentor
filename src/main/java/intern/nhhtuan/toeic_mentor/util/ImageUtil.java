package intern.nhhtuan.toeic_mentor.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Component
public class ImageUtil {
    private final Cloudinary cloudinary;

    @Value("${app.upload.dir:${user.dir}/src/main/resources/images}")
    private String uploadDir;

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

    /**
     * Save an image file to the local file system
     *
     * @param file The image file to save
     * @return The URL path to access the saved image
     * @throws IOException If an error occurs during saving
     */
    public String saveImageLocally(MultipartFile file) throws IOException {
        // Create directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate a unique filename to prevent conflicts
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".png";
        String filename = UUID.randomUUID() + extension;

        // Complete path where the file will be saved
        Path filePath = Paths.get(uploadDir, filename);

        // Save the file
        Files.write(filePath, file.getBytes());

        // Return the URL path
        return "/images/" + filename;
    }

    /**
     * Delete an image from the local file system
     *
     * @param imagePath The path of the image to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteImageLocally(String imagePath) {
        // Extract filename from path
        if (imagePath == null || imagePath.isEmpty()) {
            return false;
        }

        String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        Path filePath = Paths.get(uploadDir, filename);

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an image by deleting the old one and saving the new one
     *
     * @param file        The new image file
     * @param oldImagePath The path of the old image to replace
     * @return The URL path to access the new image
     * @throws IOException If an error occurs during saving
     */
    public String updateImageLocally(MultipartFile file, String oldImagePath) throws IOException {
        // Delete old image if it exists
        if (oldImagePath != null && !oldImagePath.isEmpty()) {
            deleteImageLocally(oldImagePath);
        }

        // Save and return the new image
        return saveImageLocally(file);
    }
}

