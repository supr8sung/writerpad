package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.entity.Comment;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void should_call_the_save_method() {
        Comment comment = new Comment.Builder().withBody("body").build();
        commentService.postComment(comment);
        verify(commentRepository).save(comment);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void should_be_able_to_get_all_comments() {
        UUID id = UUID.randomUUID();
        commentService.getAll(id);
        verify(commentRepository).findByArticleId(id);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void should_be_able_to_delete_a_comment() {
        long id = 2;
        when(commentRepository.findById(id)).thenReturn(Optional.of(new Comment()));
        commentService.deleteComment(id);
        verify(commentRepository).findById(id);
         verify(commentRepository).deleteById(id);
        verifyNoMoreInteractions(commentRepository);
    }

}
