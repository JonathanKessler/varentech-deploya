COMMENTS FROM TAV

* I like that the TODO: tag has been used to denote stuff to come back to. I find that FIXME: is useful as well. Just make sure to get back and finish these bullets!
* Don't use `e.printStackTrace()` if you are using a logging method more advanced than stdout. Use your logger instead.
```java
e.printStackTrace();  //<-- this isn't going to use your logging configuration!
logger.error("Exception while inserting entries into db: ", e);  //This uses your logging configuration and is superior!
```

* Not gona go into the code format issues. Tabs spaces, indentations, braces. Needs lots of cleanup.
* More personal preference, but the 80 character limit is for old people who only use vi. If a line of code isn't going to go to far past 80 characters don't worry about splitting it up to stick to the limit.
* JSPs look good at a glance. We will focus on the gui more later tho :)
* Add the target directory to the .gitignore, that should not ever need to be in version control.
* Personal preference, but the header comment wordage " This interface|class|method is used..." is wordy and should be avoided. Get right down to bidness.
```java
/**
 * Returns foo.
 * Takes a single arg and ignores it, returning foo.
 * @param bar - is ignored
 * @returns - foo
 */
 public String getMeSomeFoo( String bar ){
	 return "foo";
 }
```
Many shops might like the header comment that is pretty wordy, like the above, with the @param and the @returns. I don't care but lean to keeping it simple.
```java
/**
 *  Takes a single arg and ignores it, returning foo.
 */
 ```
 or even
 ```java
 /**
  * Returns foo.
  */
 ```

 Why not ```// Returns foo```? Cuz those don't show up in IDE autocomplete and IDE autocomplete is awesome. (this is at least true of eclipse)
