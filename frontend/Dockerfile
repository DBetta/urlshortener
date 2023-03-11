FROM node:lts as builder

WORKDIR /app
COPY package.json .
COPY package-lock.json .

RUN npm install

COPY . .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=builder /app/nginx/nginx.conf /etc/nginx/nginx.conf
RUN rm -rf /usr/share/nginx/html/*

COPY --from=builder /app/dist/url-shortener /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
