package bistu.rookie.duduxing.wordnote;
//合约类
import java.io.Serializable;

public class Words implements Serializable {

    // 单词、含义、例子
    private String word = "";
    private String meaning = "";
    private String sample = "";

    public Words(String word, String meaning, String sample){
        this.word = word;
        this.meaning = meaning;
        this.sample = sample;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getSample() {
        return sample;
    }

}