import { IconAt, IconPhoneCall } from '@tabler/icons-react';
import { Avatar, Group, Text } from '@mantine/core';
import classes from './ProfileIcon.module.css';

export function UserInfoIcons() {
  return (
    <div>
      <Group wrap="nowrap">
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