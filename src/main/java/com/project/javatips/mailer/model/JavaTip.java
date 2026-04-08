package com.project.javatips.mailer.model;

public class JavaTip {
    private String topicTitle;
    private String briefExplanation;
    private String codeExample;
    private String proTip;

    public JavaTip() {}

    public JavaTip(String topicTitle, String briefExplanation,
                   String codeExample, String proTip) {
        this.topicTitle = topicTitle;
        this.briefExplanation = briefExplanation;
        this.codeExample = codeExample;
        this.proTip = proTip;
    }

    public String getTopicTitle()        { return topicTitle; }
    public String getBriefExplanation()  { return briefExplanation; }
    public String getCodeExample()       { return codeExample; }
    public String getProTip()            { return proTip; }

    public void setTopicTitle(String t)        { this.topicTitle = t; }
    public void setBriefExplanation(String e)  { this.briefExplanation = e; }
    public void setCodeExample(String c)       { this.codeExample = c; }
    public void setProTip(String p)            { this.proTip = p; }
}