package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends AbstractEntity {
    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<Answer> answers = new ArrayList<>();

    private boolean deleted = false;

    public Question() {
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Question(long id, String title, String contents) {
        super(id);
        this.title = title;
        this.contents = contents;
    }


    public Question writeBy(User loginUser) {
        this.writer = loginUser;
        return this;
    }


    public List<Answer> exitsAnswerUser(User loginUser) {
        for (Answer answer : answers) {
            answer.validateConfirmWriter(loginUser);
        }
        return answers;
    }

    public void validateIsOwner(User loginUser) {
        if (!writer.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    private DeleteHistory addDeleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
    }

    public List<DeleteHistory> delete(User loginUser) {
        validateIsOwner(loginUser);
        this.deleted = true;
        List<DeleteHistory> deleteHistories = new Answers(exitsAnswerUser(loginUser)).delete();
        deleteHistories.add(addDeleteHistory());
        return deleteHistories;
    }
    public boolean isDeleted() {
        return deleted;
    }


    @Override
    public String toString() {
        return "Question [id=" + id + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }
}
