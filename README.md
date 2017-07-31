# Summarizer
📃 페이지 랭크 알고리즘을 활용하여 텍스트 본문을 특정 비율로 요약해주는 프로그램 입니다. 

## 사용방법

아래는 Summarizer 객체를 사용하는 테스트 코드 입니다.

**AppTest.java**

```java
package us.narin.summarizer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Unit test for simple Summarizer.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
        try {
            Summarizer summarizer = new Summarizer(new Scanner(new File("./test.txt")).useDelimiter("\\Z").next());
            System.out.println(summarizer.summarize());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}

```

**test.txt**
<pre>
배우 김우빈이 비인두암 투병 중인 가운데 1차 항암치료를 마친 것으로 알려졌다.

김우빈의 소속사 싸이더스HQ 측은 "김우빈이 최근 1차 항암치료를 마쳤다. 아직 결과가 나오지 않아 뭐라 밝히기가 조심스러운 상황이다. 결과를 기다리는 중이다"라고 밝혔다.

앞서 김우빈은 지난 5월 몸에 이상을 느껴 찾았던 병원에서 비인두암 진단을 받고 투병 중이다.

갑작스런 김우빈의 비인두암 투병 소식이 전해지자 팬들은 당혹감을 감추지 못했다.

특히 김우빈이 비인두암 진단 받기 전 KBS 2TV 드라마 '함부로 애틋하게'에서 시한부 신준영 역을 맡은 바 있다.

신준영 역의 김우빈은 극 중 "시간의 유한함을 안다는 건 슬프고 괴로운 일이 아니라 숨겨왔던 진심을 드러내고 용기를 낼 수 있게 하는, 내게 주어진 마지막 축복인지도 모르겠습니다"라는 독백을 하기도 했다.

해당 대사는 김우빈의 비인두암 진단과 맞물리며 많은 이들에게 공감과 위로를 안겼다.
</pre>

**출력 결과**

<pre>
[배우 김우빈이 비인두암 투병 중인 가운데 1차 항암치료를 마친 것으로 알려졌다 ., 앞서 김우빈은 지난 5월 몸에 이상을 느껴 찾았던 병원에서 비인두암 진단을 받고 투병 중이다 ., 갑작스런 김우빈의 비인두암 투병 소식이 전해지자 팬들은 당혹감을 감추지 못했다 .]
</pre>

## 의존성 설치

대한민국에서 현존하는 대부분의 형태소 분석기를 사용하기 위해 KoalaNLP 를 사용하였고, 그래프 구현을 위해 jgrpaht 를 사용하였습니다.

기본적으로 사용되는 형태소 분석기는 한나눔 한국어 형태소 분석기 입니다.

**pom.xml**
```xml
 <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-hannanum_2.12</artifactId>
      <classifier>assembly</classifier>
      <version>1.5.4</version>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-twitter_2.12</artifactId>
      <version>1.5.4</version>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-komoran_2.11</artifactId>
      <version>1.5.1</version>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-eunjeon_2.12</artifactId>
      <version>1.5.4</version>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-kkma_2.12</artifactId>
      <classifier>assembly</classifier>
      <version>1.5.4</version>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-komoran_2.12</artifactId>
      <classifier>assembly</classifier>
      <version>1.5.4</version>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-core_2.12</artifactId>
      <version>1.5.4</version>
    </dependency>
    <dependency>
      <groupId>kr.bydelta</groupId>
      <artifactId>koalanlp-kryo_2.12</artifactId>
      <version>1.5.4</version>
    </dependency>
    <dependency>
      <groupId>net.sf.jung</groupId>
      <artifactId>jung-api</artifactId>
      <version>2.1.1</version>
    </dependency>
    <dependency>
      <groupId>net.sf.jung</groupId>
      <artifactId>jung-graph-impl</artifactId>
      <version>2.1.1</version>
    </dependency>
    <dependency>
      <groupId>org.jgrapht</groupId>
      <artifactId>jgrapht-core</artifactId>
      <version>1.0.1</version>
    </dependency>
    <dependency>
      <groupId>jgraph</groupId>
      <artifactId>jgraph</artifactId>
      <version>5.13.0.0</version>
    </dependency>
```

위의 기재된 의존성 라이브러리를 메이븐을 사용하여 설치해주세요.