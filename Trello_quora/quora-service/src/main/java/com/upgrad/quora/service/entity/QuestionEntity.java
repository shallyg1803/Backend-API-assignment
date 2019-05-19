package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "question")

@NamedQueries(
        {
                @NamedQuery(name = "getAllQuestionsAnyUser", query = "select u from QuestionEntity u"),
                @NamedQuery(name = "getAllQuestions", query = "select u from QuestionEntity u where u.usersEntity.uuid=:uuid"),
                @NamedQuery(name = "userByQuestionId", query = "select u from QuestionEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "userByuserId", query = "select u from QuestionEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "findQuestionById", query = "select u from QuestionEntity u where u.uuid = :uuid"),
//                @NamedQuery(name = "questionsByUserId", query = "SELECT q from QuestionEntity q where q.usersEntity.id = :id")
        }

)
public class QuestionEntity implements Serializable {

//    insert into question (id,uuid,content,date,user_id) values(1024,'database_question_uuid','database_question_content','2018-09-17 19:41:19.593',1026);
//CREATE TABLE IF NOT EXISTS QUESTION(id SERIAL,uuid VARCHAR(200) NOT NULL, content VARCHAR(500) NOT NULL, date TIMESTAMP NOT NULL ,
//    user_id INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE);


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "content")
    @NotNull
    @Size(max= 500)
    private String content;

    @Column(name = "date")
    @NotNull
    private ZonedDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @NotNull
    private UsersEntity usersEntity;

//    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private List<AnswerEntity> answerEntity;

//    public AnswerEntity getAnswerEntity() {
//        return answerEntity;
//    }
//
//    public void setAnswerEntity(AnswerEntity answerEntity) {
//        this.answerEntity = answerEntity;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UsersEntity getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

//    public List<AnswerEntity> getAnswerEntity() {
//        return answerEntity;
//    }
//
//    public void setAnswerEntity(List<AnswerEntity> answerEntity) {
//        this.answerEntity = answerEntity;
//    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }





}

