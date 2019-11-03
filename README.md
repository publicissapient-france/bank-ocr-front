# bank-ocr-front

Front BankOCR for Xebicon19

## Development Mode

### Prerequisites

- Java 8 or later with `JAVA_HOME` set
- [Leiningen](https://github.com/technomancy/leiningen)
- Node and NPM (required by shadow-cljs)

### Install

```
npm i
```

### Run application

```
lein dev
```

shadow-cljs will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:8280](http://localhost:8280).

## Production Build

```
lein clean
lein prod
```

## Dependencies

This project relies on the [bank-ocr-back](https://github.com/xebia-france/bank-ocr-back) running on `localhost:5001`

