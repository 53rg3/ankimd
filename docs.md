





## To-do

- [ ] Bash script with `mvn` doesn't work. "Caused by: java.lang.ClassNotFoundException: english.Main"

- [x] Notes column is rendered via JS, prominently.
- [x] ~~Base64  on quotes~~
- [x] ~~Decode Base64 via JS~~
- [x] If no def or translation then render e.g. "No translations"
- [x] sound to front matter



## Markdown template

```
---
sound: [ankimd/english/some_word.mp3]
---

# word

[https://www.merriam-webster.com/dictionary/asdf](https://www.merriam-webster.com/dictionary/asdf)

- Definition 1
    - Example 1
    - Example 2
- Definition 2
- Definition 3

[https://www.dict.cc/asdf](https://www.dict.cc/asdf)

| Tables        | Are           |
| ------------- | ------------- |
| **col 3 is**  | right-aligned |
| col 2 is      | *centered*    |
| zebra stripes | ~~are neat~~  |
```
