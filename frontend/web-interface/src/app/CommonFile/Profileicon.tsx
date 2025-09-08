import { IconAt, IconPhoneCall } from '@tabler/icons-react';
import { Avatar, Group, Text } from '@mantine/core';
import classes from './ProfileIcon.module.css';

export function UserInfoIcons() {
  return (
    <div>
      <Group wrap="nowrap">
        <Avatar
          src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-2.png"
          size={48}
          radius="md"
        />
        <div>
          <Text fz="xs" tt="uppercase" fw={700} c="dimmed">
            Software engineer
          </Text>

          <Text fz="lg" fw={500} className={classes.name}>
            Robert Glassbreaker
          </Text>
        </div>
      </Group>
    </div>
  );
}