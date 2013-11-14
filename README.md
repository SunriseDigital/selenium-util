##selenium-lib
Seleniumのテストケースを作成するのに便利なユーティリティです。

Selenium Javadoc

http://selenium.googlecode.com/git/docs/api/java/index.html

###Install
cloneしてください。
```
$ git@github.com:SunriseDigital/selenium-util.git
```

このディレクトリを参照してeclipseのプロジェクトを作成し、プロジェクト直下に`library`フォルダを作成、下記のファイルをコピーし、jarファイルはビルドパスに追加して下さい。
http://code.google.com/p/selenium/downloads/list  
```
selenium-server-standalone-2.37.0.jar
IEDriverServer.exe
chromedriver.exe
```
http://www.oracle.com/technetwork/java/javamail/index.html
```
mail.jar
```


###テストケースとの関連付け
実際にテストケースを置くプロジェクトは同じワークスペースに置いてください。このプロジェクトのビルドパスにselenium-utilを追加します。対象のプロジェクトを右クリックし`Properties > Java Build Path > Projects`を選択、`Add`でselenium-utilのプロジェクトを追加します。

これだけではまだ、selenium-server-standalone-2.37.0.jarが参照できないので`Properties > Java Build Path > Libraries`を選択し、`Add JARs...`をクリックし先ほどコピーしたselenium-util/library以下のjarファイルをビルドパスに追加して下さい。（JavaMailはGmailにアクセスするクラスが使用します）

個々の環境に依存しそうなパスなどはワークスペース直下に`system.properties`というプロパティファイルを作り定義して下さい。コンストラクタに設定プロパティファイルのパスを渡すことも可能です。
このファイルの内容は直接`System.setProperty(key, value)`に渡されます。  
```
webdriver.chrome.driver = C:\\Path\\to\\workspace\\selenium-util\\library\\chromedriver.exe
webdriver.firefox.bin  = C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe
webdriver.ie.driver  = C:\\Path\\to\\workspace\\selenium-util\\library\\IEDriverServer.exe
```

###junitでの使用
`setUp`で生成して、クラス内で使いまわすと便利です。
```java
public class SomeTestCase extends TestCase
{
	private SeleniumUtil util;

	@Before
	public void setUp() throws Exception
	{	
		util = new SeleniumUtil();
		//設定プロパティファイルを指定する場合は
		//util = new SeleniumUtil("resource/settings.properties");
	}
	
	@Test
	public void testSome() throws Exception
	{
		DriverEnumerator dirivers = new DriverEnumerator();
		while (dirivers.hasMoreElements()) 
		{
			WebDriver driver = dirivers.nextElement();
			driver.get("http://***.***.***/");
			List<WebElement> ajaxList = util.waitForFindElements(driver, By.cssSelector("#ajax_list .item"));
		}
	}
```