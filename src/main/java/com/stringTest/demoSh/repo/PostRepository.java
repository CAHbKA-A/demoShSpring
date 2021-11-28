package com.stringTest.demoSh.repo;

import com.stringTest.demoSh.models.Post;
import org.springframework.data.repository.CrudRepository;



public interface PostRepository extends CrudRepository <Post,Long>  {

}
