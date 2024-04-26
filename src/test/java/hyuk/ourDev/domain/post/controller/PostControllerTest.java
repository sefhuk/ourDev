package hyuk.ourDev.domain.post.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hyuk.ourDev.domain.post.dto.PostRequestDto;
import hyuk.ourDev.domain.post.dto.PostResponseDto;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Test
    @DisplayName(value = "게시글 상세 요청")
    void postDetailsTest() throws Exception {
        //given
        Post post = Post.builder().build();
        PostResponseDto postResponseDto = PostResponseDto.builder().build();

        given(postService.findPost(1L)).willReturn(post);

        //when, then
        mockMvc.perform(get("/board/{bid}/post/{pid}", "1", "1"))
            .andExpect(model().attribute("post", postResponseDto))
            .andExpect(model().attribute("boardId", 1L))
            .andExpect(model().attribute("postId", 1L))
            .andExpect(view().name("post"));
    }

    @Test
    @DisplayName(value = "게시글 생성 페이지 요청")
    void postCreatePageTest() throws Exception {
        mockMvc.perform(get("/board/{bid}/post/new", 1L))
            .andExpect(model().attribute("id",1L))
            .andExpect(view().name("post_new"));
    }

    @Test
    @DisplayName(value = "게시글 생성 요청")
    void postAddTest() throws Exception {
        //given
        String request = "title=title1&author=author1&content=content1";

        mockMvc.perform(post("/board/{bid}/post", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(request))
            .andExpect(redirectedUrl("/board/1"));
    }

    @Test
    @DisplayName(value = "게시글 수정 페이지 요청")
    void postUpdatePageTest() throws Exception {
        mockMvc.perform(get("/board/{bid}/post/{pid}/update", 1L, 1L)
                .param("title", "title1")
                .param("content", "content1")
                .param("author", "author1"))
            .andExpect(model().attribute("title", "title1"))
            .andExpect(model().attribute("content", "content1"))
            .andExpect(model().attribute("author", "author1"))
            .andExpect(model().attribute("boardId", 1L))
            .andExpect(model().attribute("postId", 1L))
            .andExpect(view().name("post_update"));
    }

    @Test
    @DisplayName(value = "게시글 수정 요청")
    void postModifyTest() throws Exception {
        //given
        String request = "title=title1&author=author1&content=content1";

        //when, then
        mockMvc.perform(post("/board/{bid}/post/{pid}", 1L, 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(request))
            .andExpect(redirectedUrl("/board/1/post/1"));

    }

    @Test
    @DisplayName(value = "게시글 삭제 요청")
    void postRemove() throws Exception {
        mockMvc.perform(delete("/board/{bid}/post/{pid}", 1L, 1L))
            .andExpect(redirectedUrl("/board/1"));
    }
}