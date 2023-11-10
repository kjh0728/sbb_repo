package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Entity.Tag;
import com.mysite.sbb.Model.Repository.TagRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
public class TagService {

    private final TagRepository tagRepository;

    public Tag create(String name)
    {
        Tag tag = tagRepository.findByName(name);
        if(tag == null)
        {
            tag = new Tag();

            tag.setName(name);

            return tagRepository.save(tag);
        }
        return tag;
    }

    public Tag getTag(String name)
    {
        return tagRepository.findByName(name);
    }

    public List<Tag> getHotTag()
    {
        return tagRepository.getHotTag();
    }

    public List<Long> getHotTagCount()
    {
        return tagRepository.getHotTagCount();
    }
 }
