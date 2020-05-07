package com.example.android;

public class MemberDTO {
    public int image;
    public int ranking_num;
    public String name;
    public String score;

    public MemberDTO(int image, int ranking_num, String name, String score) {
        this.image = image;
        this.ranking_num = ranking_num;
        this.name = name;
        this.score = score;
    }
}
