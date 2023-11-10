package com.mysite.sbb.Service;
import com.mysite.sbb.Model.Entity.Image;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Repository.ImageRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@Builder
public class ImageService {

    private final ImageRepository imageRepository;

    public void upload(String currName, String name, String url, Member member)
    {
        Image image = imageRepository.findByMember(member);
        if(image == null)
        {
            image = new Image();
        }

        image.setCurrName(currName);
        image.setName(name);
        image.setUrl(url);
        image.setMember(member);

        this.imageRepository.save(image);
    };

    public void upload(Member member)
    {
        Image image = new Image();
        image.setMember(member);

        this.imageRepository.save(image);
    }
    public Image findImage(Member member)
    {
        return imageRepository.findByMember(member);
    };
}